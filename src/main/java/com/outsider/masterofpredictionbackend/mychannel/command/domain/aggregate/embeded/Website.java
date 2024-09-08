package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Website {
    private String website;

    protected Website() {}

    public Website(String url) {
        validate(url);
        this.website = url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        validate(website);
        this.website = website;
    }

    private void validate(String url) {
        if(url == null || url.isEmpty())
        {
            return;
        }
        if (url != null && !url.matches("^(http|https)://.*$")) {
            throw new IllegalArgumentException("Website must be a valid URL");
        }
        if (url != null && url.length() > 200) { // Assuming a reasonable max length for a URL
            throw new IllegalArgumentException("Website URL cannot exceed 200 characters");
        }
    }

    @Override
    public String toString() {
        return "Website{" +
                "website='" + website + '\'' +
                '}';
    }
}
