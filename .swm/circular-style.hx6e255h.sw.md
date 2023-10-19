---
id: hx6e255h
title: Circular Style
file_version: 1.1.3
app_version: 1.18.13
---

To make a view look circular, add the following property:

*   `app:shapeAppearanceOverlay = "@style/Theme.NewsMead.Circular"`

<br/>

Custom style for circular view
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 app/src/main/res/values/themes.xml
```xml
11         <style name="Theme.NewsMead.Circular">
12             <item name="cornerFamily">rounded</item>
13             <item name="cornerSize">50%</item>
14         </style>
```

<br/>

This is used in the following:

<br/>

The (news) source image
<!-- NOTE-swimm-snippet: the lines below link your snippet to Swimm -->
### 📄 app/src/main/res/layout/item_feed_layout.xml
```xml
19             <com.google.android.material.imageview.ShapeableImageView
20                 android:id="@+id/ivSourceImage"
21                 android:layout_width="24dp"
22                 android:layout_height="24dp"
23                 android:scaleType="fitCenter"
24                 app:shapeAppearanceOverlay="@style/Theme.NewsMead.Circular"
25                 tools:srcCompat="@tools:sample/avatars" />
```

<br/>

This file was generated by Swimm. [Click here to view it in the app](https://app.swimm.io/repos/Z2l0aHViJTNBJTNBbmV3c21lYWQlM0ElM0F1YmVyZ29ubXg=/docs/hx6e255h).