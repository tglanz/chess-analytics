package io.tglanz.chesslysis.backend.chesscom;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ClubDTO {
    @JsonProperty("@id")
    public String id;

    public String name;

    @JsonProperty("club_id")
    public Long clubId;

    public String url;
    public String icon;
    public String country;
    public String description;

    @JsonProperty("members_count")
    public Integer membersCount;

    @JsonProperty("average_daily_rating")
    public Integer averageDailyRating;

    public String visibility;
    public Long created;

    @JsonProperty("last_activity")
    public Long lastActivity;

    public List<String> admin;

    @JsonProperty("join_request")
    public String joinRequest;
}
