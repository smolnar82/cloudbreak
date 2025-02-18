package com.sequenceiq.mock.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Arguments used for disable Llama HA command.
 */
@ApiModel(description = "Arguments used for disable Llama HA command.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-12-10T21:24:30.629+01:00")




public class ApiDisableLlamaHaArguments   {
  @JsonProperty("activeName")
  private String activeName = null;

  public ApiDisableLlamaHaArguments activeName(String activeName) {
    this.activeName = activeName;
    return this;
  }

  /**
   * Name of the Llama role that will be active after HA is disabled.
   * @return activeName
  **/
  @ApiModelProperty(value = "Name of the Llama role that will be active after HA is disabled.")


  public String getActiveName() {
    return activeName;
  }

  public void setActiveName(String activeName) {
    this.activeName = activeName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiDisableLlamaHaArguments apiDisableLlamaHaArguments = (ApiDisableLlamaHaArguments) o;
    return Objects.equals(this.activeName, apiDisableLlamaHaArguments.activeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiDisableLlamaHaArguments {\n");
    
    sb.append("    activeName: ").append(toIndentedString(activeName)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

