package com.xerofox.fileviewer.binding;

import android.content.Context;
import android.databinding.DataBindingComponent;

/**
 * A Data Binding Component implementation for fragments.
 */
public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Context context) {
        this.adapter = new FragmentBindingAdapters(context);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
