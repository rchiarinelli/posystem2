/**
 *
 */
package br.com.brainyit.posystem2.restbean.response;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;

import java.io.Serializable;

/**
 * @author rafael
 *
 */
@Data
public class MessageValue extends RestBean implements Serializable {

    private String value;

}
