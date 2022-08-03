# Android - CatchCrashLib

Global interception catch exception crash
Android 全局拦截捕获异常崩溃

This library can help you locate the problems encountered in development faster and reduce the time to reproduce bugs

这个库可以帮助你更快的定位到开发中遇到的问题，减少了复现bug的时间


Step1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
		
or  in your setting.gradle (新版android studio new version )

	pluginManagement {
	    repositories {
		...
		maven { url 'https://jitpack.io' }
	    }
	}
	dependencyResolutionManagement {
	    ...
	    repositories {
		...
		maven { url 'https://jitpack.io' }
	    }
	}
	
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.liangdepeng:CatchCrashLib:v1.1'
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
    
Here are two display modes:
  1. Dialog
<div align=center><img src="https://raw.githubusercontent.com/liangdepeng/CatchCrashLib/master/example_dialog.jpg" width=432 height=936/></div>
  2. New Page
<div align=center><img src="https://raw.githubusercontent.com/liangdepeng/CatchCrashLib/master/example_page.jpg" width=432 height=936/></div>
