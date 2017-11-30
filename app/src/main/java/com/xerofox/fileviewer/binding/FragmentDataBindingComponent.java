package com.xerofox.fileviewer.binding;

import android.content.Context;
import android.databinding.DataBindingComponent;

import javax.inject.Inject;

/**
 * A Data Binding Component implementation for fragments.
 */
public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    @Inject
    public FragmentDataBindingComponent(Context context) {
        this.adapter = new FragmentBindingAdapters(context);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
