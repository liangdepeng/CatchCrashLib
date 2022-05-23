# CatchCrashLib

Global interception catch exception crash
全局拦截捕获异常崩溃

Step1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.liangdepeng:CatchCrashLib:v1.0'
	}


How to use

the First Way : Make your Application extends CrashHaldleApplication
 
    public class MyApplication extends CrashHaldleApplication {
   
        @Override
        public void onCreate() {
            super.onCreate();
        }
    }


the Other Way : In Your Application init()

    public class MyApplication extends Application {

        @Override
    	public void onCreate() {
        	super.onCreate();
        	CrashHandleUtil.init(this);
    	}
    }
    
<div align=center><img src="https://raw.githubusercontent.com/liangdepeng/CatchCrashLib/master/example_dialog.jpg" width=256 height=256/></div>


