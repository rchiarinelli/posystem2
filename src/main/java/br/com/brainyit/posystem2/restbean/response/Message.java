/**
 *
 */
package br.com.brainyit.posystem2.restbean.response;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author rafael
 *
 */
@Data
@NoArgsConstructor
public class Message extends RestBean implements Serializable {

    private String key;

    private HashMap<String, String> placeHolders;

    public Message(String key) {
        this.key = key;
    }
}
