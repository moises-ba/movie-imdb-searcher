package org.mba.challenge.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.commons.codec.binary.StringUtils;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class IMDBSearchResponse {

    private List<IMDBMovieItem> search;
    private String totalResults;
    private String response;
    private String error;


    public boolean containsError() {
        return this.error != null && this.error.trim().length() > 0;
    }

}
