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

package bisq.daonode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * Minimal data required for Bisq 2 bonded roles use case.
 * Need to be in sync with the Bisq 2 BondedRoleDto class.
 */
@Getter
@Slf4j
@Schema(title = "BondedRoleVerification")
public class BondedRoleVerificationDto {
    @Nullable
    private final String errorMessage;

    public BondedRoleVerificationDto() {
        errorMessage = null;
    }

    public BondedRoleVerificationDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
