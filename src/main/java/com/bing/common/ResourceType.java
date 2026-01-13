package com.bing.common;

public enum ResourceType {
    MP3("MP3","audio/mpeg"),
    PCM("sound","audio/wav"),
    PNG("picture", "image/png"),
    JPG("picture", "image/jpeg"),
    AUDIO("audioFromClient", "audio/mp3"),
    WORD("docxFile", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    TMP3("mp3path","audio/mpeg"),
    PCMPATH("pcmpath","audio/wav"),
    WAVPATH("wavpath","audio/wav");
    private final String directory;
    private final String defaultMimeType;

    ResourceType(String directory, String defaultMimeType) {
        this.directory = directory;
        this.defaultMimeType = defaultMimeType;
    }

    public String getDirectory() {
        return directory;
    }

    public String getDefaultMimeType() {
        return defaultMimeType;
    }
}
