
package de.dhl.onlinefrankierung.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 		Payment Results werden als key/value Paare übermittelt. Aber nur dann, wenn der Webservice entsprechend konfiguriert wurde.
 * 	
 * 
 * <p>Java-Klasse für PaymentResultsType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentResultsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="PaymentResult" type="{https://www.dhl.de/popweb/gw/ws/schema/1.0/popsc}PaymentResultType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentResultsType", propOrder = {
    "paymentResult"
})
public class PaymentResultsType {

    @XmlElement(name = "PaymentResult", required = true)
    protected List<PaymentResultType> paymentResult;

    /**
     * Gets the value of the paymentResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paymentResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPaymentResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PaymentResultType }
     * 
     * 
     */
    public List<PaymentResultType> getPaymentResult() {
        if (paymentResult == null) {
            paymentResult = new ArrayList<PaymentResultType>();
        }
        return this.paymentResult;
    }

}