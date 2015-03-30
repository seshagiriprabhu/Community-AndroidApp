package com.project.communityorganizer.utils;

import rx.functions.Func1;
import android.location.Address;
/**
 * Created by
 * @author seshagiri on 22/3/15.
 */
public class AddressToStringFunc implements Func1<Address, String> {
    /**
     * {@inheritDoc}
     * @param address
     * @return
     */
    @Override
    public String call(Address address) {
        if (address == null) return "";

        String addressLines = "";
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            addressLines += address.getAddressLine(i) + '\n';
        }
        return addressLines;
    }
}
