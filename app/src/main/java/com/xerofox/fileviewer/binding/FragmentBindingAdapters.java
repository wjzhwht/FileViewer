package com.xerofox.fileviewer.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Context context;

    @Inject
    public FragmentBindingAdapters(Context context) {
        this.context = context;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context).load(url).into(imageView);
    }
}
