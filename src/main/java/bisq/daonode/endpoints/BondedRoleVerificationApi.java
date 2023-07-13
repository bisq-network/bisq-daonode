/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.daonode.endpoints;

import bisq.common.util.Base64;
import bisq.common.util.Hex;
import bisq.core.dao.SignVerifyService;
import bisq.core.dao.governance.bond.BondState;
import bisq.core.dao.governance.bond.role.BondedRolesRepository;
import bisq.core.dao.state.DaoStateService;
import bisq.daonode.DaoNodeRestApiApplication;
import bisq.daonode.ServiceNode;
import bisq.daonode.dto.BondedRoleVerificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.security.SignatureException;

/**
 * Endpoint for getting the bonded role verification for the given parameters.
 * Used for bonded roles in Bisq 2.
 * <a href="http://localhost:8082/api/v1/bonded-roles/get-bonded-role-verification">Request the verification of a bonded role with the provided parameters</a>
 */
@Slf4j
@Path("/bonded-role-verification")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Bonded role API")
public class BondedRoleVerificationApi {
    private final DaoStateService daoStateService;
    private final BondedRolesRepository bondedRolesRepository;
    private final SignVerifyService signVerifyService;

    public BondedRoleVerificationApi(@Context Application application) {
        ServiceNode serviceNode = ((DaoNodeRestApiApplication) application).getServiceNode();
        daoStateService = serviceNode.getDaoStateService();
        bondedRolesRepository = serviceNode.getBondedRolesRepository();
        signVerifyService = serviceNode.getSignVerifyService();
    }

    @Operation(description = "Request the verification of a bonded role with the provided parameters")
    @ApiResponse(responseCode = "200", description = "A BondedRoleVerification result object",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(allOf = BondedRoleVerificationDto.class))}
    )
    @GET
    @Path("get-bonded-role-verification/{user-name}/{role-type}/{message}/{signature-hex}")
    public BondedRoleVerificationDto getBondedRoleVerification(@PathParam("user-name") String userName,
                                                               @PathParam("role-type") String roleType,
                                                               @PathParam("message") String nickName,
                                                               @PathParam("signature-hex") String signature) {
        String signatureBase64 = Base64.encode(Hex.decode(signature));
        return bondedRolesRepository.getAcceptedBonds().stream()
                .filter(bondedRole -> bondedRole.getBondedAsset().getBondedRoleType().name().equals(roleType))
                .filter(bondedRole -> bondedRole.getBondedAsset().getName().equals(userName))
                .filter(bondedRole -> bondedRole.getBondState() == BondState.LOCKUP_TX_CONFIRMED)
                .flatMap(bondedRole -> daoStateService.getTx(bondedRole.getLockupTxId()).stream())
                .map(tx -> tx.getTxInputs().get(0))
                .map(txInput -> {
                    try {
                        signVerifyService.verify(nickName, txInput.getPubKey(), signatureBase64);
                        return new BondedRoleVerificationDto();
                    } catch (SignatureException e) {
                        return new BondedRoleVerificationDto("Signature verification failed.");
                    }
                }).findAny()
                .orElse(new BondedRoleVerificationDto("Did not find a bonded role matching the parameters"));
    }
}
