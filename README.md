# bisq-daonode

## Overview

TODO

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


