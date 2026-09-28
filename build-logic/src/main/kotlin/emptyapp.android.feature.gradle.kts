plugins {
    // No plugins declared here: the sibling convention plugins are applied
    // imperatively below so they don't need to be resolved as plugin requests.
}

apply(plugin = "emptyapp.android.library")
apply(plugin = "emptyapp.android.compose")
apply(plugin = "emptyapp.android.hilt")
