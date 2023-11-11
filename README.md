# Bisq DAO node

## Overview

The Bisq DAO node provides access to Bisq network data as well as Bisq DAO data via a REST API.
It is used for Bisq 2 to request data about the DAO state as well as account age and account witness data for reputation use cases.


## Building source code

This repo has a dependency on git submodules [bisq](https://github.com/bisq-network/bisq)
and [bisq-gradle](https://github.com/bisq-network/bisq-gradle).  
There are two ways to clone it before it can be compiled:

```
# 1) Use the --recursive option in the clone command:
$ git clone --recursive  https://github.com/bisq-network/bisq-daonode.git

# 2) Do a normal clone, and pull down the bisq repo dependency with two git submodule commands:
$ git clone https://github.com/bisq-network/bisq-daonode.git
$ cd bisq-daonode
$ git submodule init
$ git submodule update
```

To build:

```
$ ./gradlew clean build
```

To update submodule:
```
$ git pull --recurse-submodules
$ git submodule update --remote
```


Program arguments to run 'DaoNodeApplication' with Bitcoin Regtest and localhost mode:
```
--baseCurrencyNetwork=BTC_REGTEST 
--useDevPrivilegeKeys=true 
--useLocalhostForP2P=true 
--appName=[your app name]
--fullDaoNode=true 
--rpcUser=[Bitcoin rpc username] 
--rpcPassword=[Bitcoin rpc password] 
--rpcPort=18443 
--rpcBlockNotificationPort=[port used in blocknotify]
```

To run the 'DaoNodeApplication' you need to have Bitcoin node running and have 'blocknotify' in the `bitcoin.conf` set up.