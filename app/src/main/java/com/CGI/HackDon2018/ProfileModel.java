package com.CGI.HackDon2018;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileModel {
    @SerializedName("profileId")
    public Integer profileId;
    @SerializedName("lastName")
    public String lastName;
    @SerializedName("gender")
    public String gender;
    @SerializedName("email")
    public String email;
    @SerializedName("affinities")
    ArrayList<Integer> affinities;
}
