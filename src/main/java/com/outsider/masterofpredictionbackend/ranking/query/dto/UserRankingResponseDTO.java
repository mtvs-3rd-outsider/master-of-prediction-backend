import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserRankingResponseDTO {
    private Long userId;
    private BigDecimal points;
    private Long rank;
} 