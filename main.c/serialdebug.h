#ifndef __SERIAL_DEBUG_H
#define __SERIAL_DEBUG_H

#define DEBUG_ENABLE

#ifdef DEBUG_ENABLE
#warning Debug enabled!

char debugBuffer[50];

/* Para ativar ou desativar mensagens de debug basta comentar as definicoes */
#define __DEBUG_APP
//#define __DEBUG_MAIN

#endif /* DEBUG_ENABLE */

#endif /* __SERIAL_DEBUG_H */
