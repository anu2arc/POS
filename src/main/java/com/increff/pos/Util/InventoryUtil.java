package com.increff.pos.Util;

import com.increff.pos.model.Form.InventoryForm;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryUtil {
    public void validate(InventoryForm form) throws ApiException {
        if(form.getQuantity()==null)
            throw new ApiException("Quantity cannot be Null");
        if(form.getQuantity()<0)
            throw new ApiException("Quantity cannot be negative");
        if(form.getQuantity()>1000000)
            throw new ApiException("Quantity exceeded the max limit");
    }
}
