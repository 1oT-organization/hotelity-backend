package org.iot.hotelitybackend.sales.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membership_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer membershipLevelCodePk;
    private String membershipLevelName;
    private String membershipInfo;
    private Integer membershipCriteriaAmount;

	@Builder
	public MembershipEntity(Integer membershipLevelCodePk, String membershipLevelName, String membershipInfo,
		Integer membershipCriteriaAmount) {
		this.membershipLevelCodePk = membershipLevelCodePk;
		this.membershipLevelName = membershipLevelName;
		this.membershipInfo = membershipInfo;
		this.membershipCriteriaAmount = membershipCriteriaAmount;
	}
}