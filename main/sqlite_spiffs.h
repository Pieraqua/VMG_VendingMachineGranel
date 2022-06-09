#include <stdio.h>
#include <stdlib.h>
#include <sqlite3.h>
#include <SPI.h>
#include <FS.h>
#include "SPIFFS.h"

/* You only need to format SPIFFS the first time you run a
   test or else use the SPIFFS plugin to create a partition
   https://github.com/me-no-dev/arduino-esp32fs-plugin */
#define FORMAT_SPIFFS_IF_FAILED true


static int callback(void *data, int argc, char **argv, char **azColName);
int db_open(const char *filename, sqlite3 **db);
int db_exec(sqlite3 *db, const char *sql);
void sqliteInit();
