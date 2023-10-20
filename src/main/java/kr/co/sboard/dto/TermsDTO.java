package kr.co.sboard.dto;

import kr.co.sboard.entity.TermsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermsDTO {
    private int no;
    private String privacy;
    private String terms;

    public TermsEntity toEntity(){
        return TermsEntity.builder()
                .no(no)
                .privacy(privacy)
                .terms(terms)
                .build();
    }
}
