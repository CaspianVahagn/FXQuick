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

Allows you to generate a view by FXML. 
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

Dock a Parent or Nodeproperty to the View to make Switching possible: 

```java
	 FXView view = new ExampleView();
	 BorderPane bp = new BorderPane();
	 view.dock(bp.centerProperty()); 
````

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

```
