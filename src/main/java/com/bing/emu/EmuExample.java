package com.bing.emu;

public enum EmuExample {
    ONE("1"){
        void run(){

        }
    },
    TWO("1"){
        void run(){

        }
    };

    String code;
    abstract void run();
    EmuExample(String s) {
    }

    public static EmuExample of(String code){
        for (EmuExample emu:EmuExample.values()){
            if(code== emu.code){
                return emu;
            }
        }
        return  null;
    }
}
