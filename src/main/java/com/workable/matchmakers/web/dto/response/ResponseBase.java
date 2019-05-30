package com.workable.matchmakers.web.dto.response;

import io.swagger.annotations.ApiModelProperty;
import com.workable.matchmakers.web.enums.Result;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ResponseBase
 */
public class ResponseBase {

  public static ResponseBase Builder() {
    return new ResponseBase();
  }

  @NotNull
  @ApiModelProperty(required = true)
  private Result result;

  @NotNull
  @ApiModelProperty(required = true)
  private String description;

  public ResponseBase result(Result result) {
    this.result = result;
    return this;
  }

  public ResponseBase build(Result result) {
    this.result = result;
    this.description = result.getDescription();
    return this;
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public ResponseBase description(String description) {
    this.description = description;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBase responseSuccess = (ResponseBase) o;
    return Objects.equals(this.result, responseSuccess.result) &&
        Objects.equals(this.description, responseSuccess.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, description);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}

