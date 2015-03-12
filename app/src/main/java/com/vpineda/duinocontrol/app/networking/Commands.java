package com.vpineda.duinocontrol.app.networking;

/**
 * Created by vpineda1996 on 2015-02-08.
 */
public enum Commands {
    DIGITAL_WRITE  (1),
    DIGITAL_READ   (2),
    ANALOG_WRITE   (3),
    ANALOG_READ    (4);

    private int command;
    Commands (int command){
        this.command = command;
    }

    public int getCommand() {
        return command;
    }
}
//switch(cmdid) {
//        case 0:  sm(pin,val);              break;
//        case 1:  dw(pin,val);              break;
//        case 2:  dr(pin,val);              break;
//        case 3:  aw(pin,val);              break;
//        case 4:  ar(pin,val);              break;
//        case 96: handleRCTriState(pin, val); break;
//        case 97: handlePing(pin,val,aux);  break;
//        case 98: handleServo(pin,val,aux); break;
//        case 99: toggleDebug(val);         break;
//default:                           break;
//        }