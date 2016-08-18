package org.docksidestage.app.web.mypage;

import org.docksidestage.dbflute.exentity.Product;
import org.docksidestage.mylasta.helper.MyToString;
import org.lastaflute.web.validation.Required;

/**
 * @author jflute
 */
public class MypageProductBean {

    @Required
    public final String productName;
    @Required
    public final Integer regularPrice;

    public MypageProductBean(Product product) {
        this.productName = product.getProductName();
        this.regularPrice = product.getRegularPrice();
    }

    @Override
    public String toString() {
        return MyToString.of(this);
    }
}
