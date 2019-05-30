package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.web.validator.RegexValidator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * CandidateBaseDto
 */
@JsonPropertyOrder({ "username", "password", "name", "email", "cellphone", "linkedInUrl", "facebookUrl", "education", "experience", "objective" })
@Getter
@Setter
public class CandidateBaseDto {

	@NotEmpty
	@ApiModelProperty(example = "panto", required = true)
	private String username;

	// @NotEmpty
	@ApiModelProperty(example = "password", required = true)
	@ToString.Exclude
	private String password;

	@NotEmpty
	@Pattern(regexp=RegexValidator.EMAIL_VALIDATOR, flags = Pattern.Flag.CASE_INSENSITIVE, message="Invalid email address!")
	@ApiModelProperty(example = "panos@antonakos.com", required = true)
	private String email;

	@ApiModelProperty(example = "Panos Antonakos")
	private String name;

	@ApiModelProperty(example = "00306941234567")
	private String cellphone;

	@ApiModelProperty(example = "https://www.linkedin.com/in/antonakospanos/")
	@Pattern(regexp=RegexValidator.LINKEDIN_URL_VALIDATOR, flags = Pattern.Flag.CASE_INSENSITIVE, message="Invalid LinkedIn URL!")
	private String linkedInUrl;

	@ApiModelProperty(example = "https://www.facebook.com/profile.php?id=100012244173153/")
	@Pattern(regexp=RegexValidator.FACEBOOK_URL_VALIDATOR, flags = Pattern.Flag.CASE_INSENSITIVE, message="Invalid Facebook URL!")
	private String facebookUrl;

	private List<EducationDto> education;

	private ExperienceDto experience;

	private ObjectiveDto objective;


	public CandidateBaseDto() {
	}

	public CandidateBaseDto(String username, String password, String name, String email,
							String cellphone, String linkedInUrl, String facebookUrl,
							List<EducationDto> education, ExperienceDto experience, ObjectiveDto objective) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.cellphone = cellphone;
		this.linkedInUrl = linkedInUrl;
		this.facebookUrl = facebookUrl;
		this.education = education;
		this.experience = experience;
		this.objective = objective;
	}
}
