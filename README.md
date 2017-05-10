![cr_md](https://cloud.githubusercontent.com/assets/13011023/17073167/d9f8f01e-508b-11e6-9bf3-0ed7a98d58f0.png)
# CrackRetail-Android-SDK
- [Prerequisites](#prerequisites)
- [Installation](#installation)
    * [Gradle Installation](#gradleinstallation)
    * [Jar Installation](#jarinstallation)
- [Permission Addition](#permissionaddition)
- [Usage](#usage)
   * [Banner Ad](#bannerad)
         * [Standard Banner](#standardbanner)
         * [Banner XML Layout](#bannerxmllayout)
         * [Banner Activity](#banneractivity)
   * [Interstitial Ad](#interstitialad)
         * [Interstitial Activity](#interstitialactivity)
   * [Native Ad](#nativead)
         * [Native Component](#nativecomponent)
         * [Native XML Layout](#nativexmllayout)
         * [Native Activity](#nativeactivity)
         * [NativeAd Methods](#nativeadmethod)
   
#<a id="prerequisites"></a>Prerequisites
You will need a [CrackRetail](https://www.crackretail.com) account

#<a id="installation"></a>Installation

##1.<a id="gradleinstallation"></a> Gradle Installation
CrackRetail SDK can be easily be added as a dependency
 - add `Google Play Services` and `CrackRetail-Android-SDK` to your Compile dependencies:
 
 ```java
   dependencies{
       // compile other depencies
       compile 'com.google.android.gms:play-services:8.4.0'
       compile '' //compile path will be provided after jar release at github
   }
 ```
 
##2.<a id="jarinstallation"></a> Jar Installation
  - Download and unzip CrackRetail-Android-SDK Lib and extract the **`CrackRetail-Android-SDK.jar`**
  - Add the jar file to assests of Project 
  - Add `Google Play Services` and `CrackRetail-Android-SDK` to your Compile dependencies:
     
     ```java
     dependencies{
          compile 'com.google.android.gms:play-services:8.4.0'
          compile files('src/main/assets/CrackRetail-Android-SDK.jar')   //if jar file is in assets otherwise foldername will in place of assets
          //compile other dependencies...
     }
     ```
 

 
#<a id="permissionaddition"></a>Permission Addition
 In your project's `AndroidManifest.xml` under manifest tag, add the following permissions:
 
 ```xml
 
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
    
    ...
    <application>
    ...
    
    <!--add Crackretail Interstitial Activity if implementing Interstitial Ad -->
    <activity android:name="com.crackretail.sdk.interstitialads.InterstitialActivity" />
    
    ...
    </applicaton>
    
    
 ```
#<a id="usage"></a>Usage
##<a id="bannerad"></a>Banner
###<a id="standardbanner"></a>Standard Banner
| Type        | Size(WxH)           | Description  |
| ------------- |:-------------:| -----:|
| Type I     | 310x250 | Extra Large Banner |
| Type II      | 310x150 | Large Banner |
| Type III      | 310x100 | Medium Banner |
| Type IV      | 310x70 | Small Banner |
| Type V     | 310x50 | Very Small Banner |
| Type VI      | screenwidth x50or70 | Smart Banner |

### <a id="bannerxmllayout"></a>Activity's Layout XML setup:

  ```xml
   
       <com.crackretail.sdk.Banner
              android:id="@+id/mybanner"
              android:layout_width="310dp"
              android:layout_height="260dp"
              android:gravity="center"
              android:layout_gravity="center">
       </com.crackretail.sdk.Banner>      
   
  
   ```
 >It's advised to use [Standard banner](#standardbanner) combination of layout width X layout height, Popular sizes are described [above](#standardbanner)
 
### <a id="banneractivity"></a>Activity setup:
 
 ```java
 
 // ...
 
 import com.crackretail.sdk.Banner;
 import com.crackretail.sdk.bannerads.BannerListener;
 
 // ...
 
 
   Banner banner = null;
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        banner = (Banner) findViewById(R.id.mybanner);
        
        banner.setListener(new BannerListener() {
            @Override
            public void onBannerError(View view, Exception exception) {

                Toast.makeText(bannershow.this, "" + exception.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBannerLoaded(View view) {

                Toast.makeText(bannershow.this, "Banner Loaded!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBannerClosed(View view) {
                Toast.makeText(bannershow.this, "Banner Closed!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBannerFinished() {
                Toast.makeText(bannershow.this, "Banner Finished", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBannerClicked(View view) {
                Toast.makeText(bannershow.this, "Banner Clicked!", Toast.LENGTH_LONG).show();
                

            }

            @Override
            public void onNoFill(View view) {
                Toast.makeText(bannershow.this, "Banner No fill!", Toast.LENGTH_LONG).show();
            }
        });

        banner.setInventoryHash("<your-publication-hash>");
        banner.load();

    }
    
    //permission dialog for marshmello and above
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    banner.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
 
 
 ```
 
##<a id="interstitialad"></a>Interstitial Ad
### <a id="interstitialactivity"></a>Activity setup: 

```java

//...

import com.crackretail.sdk.Interstitial;
import com.crackretail.sdk.interstitialads.InterstitialAdListener;

//...


Interstitial interstitialad=null;
InterstitialAdListener listener=null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitialshow);


        interstitialad = new Interstitial(this);

        final Activity self = this;
        listener = new InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(Interstitial interstitial) {
                Toast.makeText(self, "loaded", Toast.LENGTH_SHORT).show();
                //call show() to display the interstitial when its finished loading
                interstitial.show();
            }
            @Override
            public void onInterstitialFailed(Interstitial interstitial, Exception e) {
                Toast.makeText(self, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialClosed(Interstitial interstitial) {
                Toast.makeText(self, "closed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialFinished() {
                Toast.makeText(self, "finished", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialClicked(Interstitial interstitial) {
                Toast.makeText(self, "clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onInterstitialShown(Interstitial interstitial) {
                Toast.makeText(self, "shown", Toast.LENGTH_SHORT).show();
            }
        };
        interstitialad.setListener(listener);
        interstitialad.setInventoryHash("<your-publication-hash>");
        interstitialad.load();


    }

      //permission dialog for marshmello and above
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


```

In your project's **AndroidManifest.xml** under the **application** tag, declare the following activity:

```xml

<activity android:name="com.crackretail.sdk.interstitialads.InterstitialActivity" />

```

#<a id="nativead"></a>Native Ad

###<a id="nativecomponent"></a>Component of Native Ad
| Component        | Type           | Description  |
| ------------- |:-------------:| -----:|
| icon     | ImageAssets | 250x250 Icon Image, can be scaled down |
| main      | ImageAssets | 1200x627 Image, please scale down to fit screen |
| headline      | TextAssets | Headline (maximum 30 characters) |
| description      | TextAssets | Description (maximum 200 characters) |
| cta     | TextAssets | Call-to-Action Text (e.g. 'Install Now') |
| advertiser      | TextAssets | Name of the Advertiser |
| rating      | TextAssets | Rating Numbers 1-5 (e.g for app ratings) |

### <a id="nativexmllayout"></a>Activity's Layout XML setup:

  ```xml
  
  
     <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginRight="10dp"
        android:layout_marginBottom="10dp"
        android:layout_marginTop="3dp"
        android:background="@drawable/layout_back"
        android:id="@+id/myadLayout"
        >


           

        <ImageView
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:id="@+id/icon_imageView"
            android:src="@drawable/icon"
            android:layout_marginTop="10dp"
            android:layout_alignParentTop="true"
            android:layout_alignParentLeft="true"
            android:layout_alignParentStart="true" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:text="Ad Heading"
            android:id="@+id/textView_Heading"
            android:textStyle="bold"
            android:layout_marginTop="4dp"
            android:layout_alignTop="@+id/icon_imageView"
            android:layout_toRightOf="@+id/icon_imageView"
            android:layout_toEndOf="@+id/icon_imageView"
            android:layout_marginLeft="25dp"
            android:layout_marginStart="25dp" />

        <RatingBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/ratingBar"
            android:layout_marginTop="3dp"
            style="?android:attr/ratingBarStyleSmall"
            android:rating="4.5"
            android:layout_alignLeft="@+id/textView_Heading"
            android:layout_alignStart="@+id/textView_Heading"
            android:layout_below="@+id/textView_Heading" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textAppearance="?android:attr/textAppearanceSmall"
                android:text="this is the sample text which is to be fetched from the server to the ad content advertised by advertiser"
                android:id="@+id/textView3"
                android:layout_marginTop="3dp"
                android:layout_marginBottom="2dp"
                android:layout_below="@+id/icon_imageView"
                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true" />

            <ImageView
                android:src="@drawable/main"
                android:layout_width="match_parent"
                android:layout_height="180dp"
                android:id="@+id/imageView3"
                android:layout_below="@+id/textView3"
                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true" />

            <Button
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="install"
                android:id="@+id/button_install"
                android:layout_below="@+id/imageView3"
                android:layout_alignRight="@+id/linearLayout"
                android:layout_alignEnd="@+id/linearLayout" />


        </RelativeLayout>


  ```
  
  
###<a id="nativeactivity"></a>Activity Setup:
  
  ```java
  
    //...
    
     import com.crackretail.sdk.customevents.CustomEventNative;
     import com.crackretail.sdk.nativeads.Native;
     import com.crackretail.sdk.nativeads.NativeAd;
     import com.crackretail.sdk.nativeads.NativeListener;
    
    //...
    
    
    
    private RelativeLayout layout=null;
    private ImageView iconimage=null;
    private ImageView mainimage=null;
    private TextView tv_heading=null;
    private TextView tv_description=null;
    private RatingBar ratingBar=null;
    private Button btn_callaction=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nativeshow);

        layout=(RelativeLayout)findViewById(R.id.myadLayout);
        layout.setVisibility(View.GONE);
        iconimage=(ImageView)findViewById(R.id.icon_imageView);
        mainimage=(ImageView)findViewById(R.id.imageView3);
        tv_heading=(TextView)findViewById(R.id.textView_Heading);
        tv_description=(TextView)findViewById(R.id.textView3);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        btn_callaction=(Button)findViewById(R.id.button_install);



        Native mynative=new Native(this);
        mynative.setListener(new NativeListener() {
            @Override
            public void onNativeReady(Native nativevar, CustomEventNative customEventNative, final NativeAd nativeAd) {
                
                //registering layout with ClickURL
                customEventNative.registerViewForInteraction(layout);
                
                //firing the trackers
                nativeAd.fireTracker(ActivityName.this);
                
                //setting content to the component of the layout
                iconimage.setImageBitmap(nativeAd.getIcon());
                mainimage.setImageBitmap(nativeAd.getMain());
                tv_heading.setText(nativeAd.getHeadline());
                tv_description.setText(nativeAd.getDescription());
                ratingBar.setRating(Float.parseFloat(nativeAd.getRating().trim()));
                btn_callaction.setText(nativeAd.getCta());
                
                //setting clickURL to the Call to Action Button
                btn_callaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(nativeAd.getClickUrl());
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        nativeshow.this.startActivity(intent);

                    }
                });
              
                layout.setVisibility(View.VISIBLE);
                Toast.makeText(nativeshow.this,"Native Ad loaded!",Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNativeError(Exception var1)
            {
                Toast.makeText(nativeshow.this,"Error Loading Ad!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNativeClick(NativeAd var1)
            {
                Toast.makeText(nativeshow.this,"Native Ad Clicked!",Toast.LENGTH_LONG).show();
            }
        });

        mynative.setInventoryHash("<your-publication-hash>");
        mynative.load();




    }
    
  
  
  ```
  
###<a id="nativeadmethod"></a>NativeAd Methods:
  
  The **`NativeAd`** object returned by the native listener contains the ad data used to constructed the native ad 
  these following method must be invoked from the **onReady() listener** of tne NativeListener
  
  
  ```java
  
  public class NativeAd {

   // WARNING : These method must be invoked from onReady listener of Native 
                otherwise null will be returned by respective method
     
     //Get Icon image url of the icon
     public String getIconUrl();
    
     //Get Icon Width
     public int getIconWidth();

     //Get Icon Height
     public int getIconHeight();

     //Get Bitmap of Icon Image
     public Bitmap getIcon();

     //Get Main image url of the main bitmap
     public String getMainUrl();

     //Get Main bitmap width
     public int getMainWidth()

     //Get Main bitmap height
     public int getMainHeight();

     //Get Main bitmap 
     public Bitmap getMain();

     //Get Headline of Native Ad  
     public String getHeadline();

      //Get Description of Native Ad
     public String getDescription();

      //Get Call To Action(CTA) Text 
     public String getCta();

      //Get Rating of RatingBar of Native Ad
     public String getRating();

      //Get Advertiser Name of the Native Ad
     public String getAdvertiser();

      //Get List of the trackers
     public List<Tracker> getTrackerList();

      //Get ClickURL
     public String getClickUrl();

}
  

  ```
  
  **NOTE**: The `List<Tracker>` returned by getTrackerList contains tracker url's you must call before displaying
   the ad by calling the **`NativeAd.fireTrackers`** method. 
  
  
  
  
  


