package com.CGI.HackDon2018;

import com.google.gson.annotations.SerializedName;

public class QuestionModel {
    @SerializedName("profileAffinityId")
    public Integer profileAffinityId;
    @SerializedName("profileAffinityName")
    public String profileAffinityName;
    @SerializedName("profileAffinityType")
    public String profileAffinityGroup;

    public boolean selected;

}
