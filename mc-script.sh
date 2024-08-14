#!/bin/bash

mc alias set myminio https://localhost:9000 MY-USER MY-PASSWORD
mc ls myminio/mybucket