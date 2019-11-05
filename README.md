# FXQuick
Quick Helper for JavaFX with Dependencyinjection and Runtime Injection

get it via Jitpack:

```
 repositories {

    //....
    maven { url 'https://jitpack.io' }
}

dependencies {
    //...
    compile 'com.github.luxusproblem:FXQuick:[version]'
}

```

## Dependency Injection via @FXInject

Scan your packages on Application Start with FXConfiguration: 
```java


FXConfigration.scanServices("your.services.package");
FXConfigration.scanRuntimeInjections("your.controller.packageWithInjections");
FXConfigration.apply();

```

Annotate Classes with @FXService or @FXController to prepare them or Inject them.

```java
@FXService
public class SampleService {
	
	
	@FXInject
	OtherService service;
	
	public void test() {
		System.out.println("twerk it");
		service.test();
	}

}

@FXService
public class OtherService {

	
	public void test() {
		System.out.println("It works");
	}
}
```


## FXView 

An Interface that allows you to generate a view by FXML. 
No need to declare a own Controller in the FXML anymore.

```java

public class SampleBasicView extends FXView{
	
	@FXML
	private Text helloWorld;
	
	@Override
	public void init() {
			loadFXML("view/sample.fxml");
	}
	
	public void basicAction(ActionEvent e) {
		switchTo(new SampleBasicView2());
	}
}

```
### Easy View switching

Allows you to switch views after Docking to Parent Property easily. With "switchTo(FXView)".

Initial Docking to a Parent or Nodeproperty to the View to make Switching possible: 
>(this is only nessecary if your view is docked to the sceen as "entry point" to the Scengraph)

```
		FXView view = new SampleBasicView();
		Parent root = (Parent) view.getRoot();

		Scene scene = new Scene(root, 1200, 900);
		
		view.dock(scene.rootProperty());

```
Call *switchTo(FXView)* in the view for example via button click.

```
	public void basicAction(ActionEvent e) {
		switchTo(new SampleBasicView2());
	}

```

The same operation works with every other Parentelement for example BorderPane, TabPane, Hbox, Vbox etc without docking.

### IncludeView via FXML:
You don't need to declare Includes anymore with <fx:id include... />

You can use:
```xml

<BorderPane>
	<left>
	   <IncludeView viewName="your.views.package.SampleChartView" />
	</left>
        <center>
	     <IncludeView viewName="your.views.package.SampleBasicView" />
	</center>
</BorderPane>

```
You can also configure the Namespace for you viewPackages in the FXConfigurations on Applicationstart

```
FXConfigration.addViewNameSpaces("your.views.package.iViewsImpl","other.views.package");

```
now you don't have to use the whole qualified Name. 

**NOTE:** 

FXViews should have a unique Name to avoid problems with Package call.
If the Names of views are ambiguous You MUST use the whole qualified Name.  


```xml

<BorderPane>
	<left>
	   <IncludeView viewName="SampleChartView" />
	</left>
        <center>
	     <IncludeView viewName="SampleBasicView" />
	</center>
</BorderPane>

```

### "async await"-like feature for Threadsafe calls outside of the UI Thread

```java

public class SampleBasicView2 extends FXView {

	
	@FXML
	Text title;

	@FXInject
	SampleService sampleService;

	@Override
	public void init() {

		loadFXML("view/samle2.fxml");

		generateSampleData();

	}

	public void basicAction(ActionEvent e) {
		sampleService.test();
		async(()->{
			Thread.sleep(3000);
			return "hello " + Math.random(); 
			
		}).await(val ->{
			title.setText(val);
		});
	}
}
```

#### Async ErrorHandling 

In the Likelyhood of an exception or error in a async Function you should also be able to Debug it.

```java

	public void basicAction(ActionEvent e) {
		
		async(()->{
			Thread.sleep(3000);
			int[] arr= {1,2,3};
			//will throw array out of bounds exception
			return "hello" + arr[7]; 
			
		}).await((err,val) ->{
		
			if(err != null){
			   // handle error
			}else{
			   title.setText(val);
			
			}
			
		});
	}
}
```

You also have the Option for a async call with timeout in millisecond

```java

		async(2000,()->{
			Thread.sleep(3000);
			
			//will throw timeoutException
			return "hello"; 
			
		}).await((err,val) ->{
			if(err != null){
			   // handle error
			}else{
			   title.setText(val);
			
			}
			
		});


```
