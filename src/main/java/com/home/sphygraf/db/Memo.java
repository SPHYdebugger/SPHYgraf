package com.home.sphygraf.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;

public class Memo implements JsonSerializable{

    @JsonProperty
    private String pathOriginal;
    @JsonProperty
    private boolean bAndW;
    @JsonProperty
    private boolean invertColors;
    @JsonProperty
    private boolean shineUp;
    @JsonProperty
    private boolean invertH;
    @JsonProperty
    private boolean invertV;
    @JsonProperty
    private boolean blurred;
    @JsonProperty
    private LocalDate date;



    public Memo(String pathOriginal, boolean bAndW, boolean invertColors, boolean shineUp, boolean invertH, boolean invertV, boolean blurred, LocalDate date) {
        this.pathOriginal = pathOriginal;
        this.bAndW = bAndW;
        this.invertColors = invertColors;
        this.shineUp = shineUp;
        this.invertH = invertH;
        this.invertV = invertV;
        this.blurred = blurred;
        this.date = date;

    }


    public String getPathOriginal() {
        return pathOriginal;
    }

    public boolean isbAndW() {
        return bAndW;
    }

    public boolean isInvertColors() {
        return invertColors;
    }

    public boolean isShineUp() {
        return shineUp;
    }

    public boolean isInvertH() {
        return invertH;
    }

    public boolean isInvertV() {
        return invertV;
    }

    public boolean isBlurred() {
        return blurred;
    }

    public LocalDate getDate() { return date; }


}
