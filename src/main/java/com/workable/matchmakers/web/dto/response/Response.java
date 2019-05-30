package com.workable.matchmakers.web.dto.response;

import io.swagger.annotations.ApiModelProperty;
import com.workable.matchmakers.web.enums.Result;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Response
 */
public class Response extends ResponseBase {

  public static Response Builder() {
    return new Response();
  }

  @ApiModelProperty(required = true, notes = "Data created by the request")
  private Object data;

  public Response build(Result result) {
    setResult(result);
    setDescription(result.getDescription());
    return this;
  }

  public Response result(Result result) {
    setResult(result);
    return this;
  }

  public Response description(String description) {
    setDescription(description);
    return this;
  }

  public Response data(Object data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Response)) return false;
    if (!super.equals(o)) return false;

    Response response = (Response) o;

    return data != null ? data.equals(response.data) : response.data == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (data != null ? data.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}

