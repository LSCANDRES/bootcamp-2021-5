package viajesfalabella.equipo4;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;




public class Alojamientos{

    private WebDriver driver;
    private WebDriverWait wait;
    private ArrayList<String> tabs2;
    private List<WebElement> categorias;

    @BeforeClass
    public static void Setup(){
        System.out.println("Setup necesario antes de Instanciar");
        WebDriverManager.chromedriver().setup();

    }

    @Before
    public void init(){
        System.out.println("init para instanciar");
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.get("https://www.viajesfalabella.cl/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,10);
        categorias  = driver.findElements(By.cssSelector("div.header-products-container ul li a"));

    }

    @Test
    public void alojamientoBuscarSegunMejorPuntuacionYMostrarFAQ() throws InterruptedException {
        //Obtenemos una lista con todas las categorias y le hacemos click cuando encuentre Alojamientos
        busqueda(categorias,"Alojamientos");

        //Ingresamos Londres en la caja de busqueda Destino
       WebElement destino = driver.findElement(By.cssSelector("div.input-container .sbox-destination"));
       destino.sendKeys("Londres");
       //Esperamos a que se despliegue la lista y le damos ENTER a la primera opcion
       wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.item")));
       destino.sendKeys(Keys.ENTER);

       //Click en el checkbox Todavía no he decidido la fecha
        driver.findElement(By.xpath("//label[@class='checkbox-label']")).click();
        //Click en el boton Buscar
        driver.findElement(By.xpath("//em[contains(text(),'Buscar')]")).click();

        //Filtramos los resultados por Mejor puntuacion
        Select s1 = new Select(driver.findElement(By.id("sorting")));
        s1.selectByVisibleText("Mejor puntuación");

        //Filtramos los resultados por moneda Dolar
        Select s = new Select(driver.findElement(By.id("currency")));
        s.selectByVisibleText("Dólar");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[starts-with(@class,'checkbox-label')]")));

        //Click en el alojamiento ingresado y cambio de pestana
        busquedaAlojamiento("The Chelsea Harbour Hotel");

        //Obtenemos una lista de cada FAQ y las desplegamos una por una
        int count = driver.findElements(By.cssSelector(".-detail-section-padding .dropdown-item")).size();
        for (int i = 0; i < count; i++) {
            driver.findElements(By.cssSelector(".-detail-section-padding .dropdown-item")).get(i).click();
        }

        //Capturamos el titulo del alojamiento seleccionado
        String TituloHotel = driver.findElement(By.xpath("//span[@class='accommodation-name eva-3-h2']")).getText();
        String checkTituloHotel = "The Chelsea Harbour Hotel";

        Assert.assertEquals(checkTituloHotel,TituloHotel);

    }

    @Test
    public void alojamientosBuscarMenorAMayorPrecioYConWiFiGratis() throws InterruptedException {
        //Obtenemos una lista con todas las categorias y le hacemos click cuando encuentre Alojamientos
        busqueda(categorias,"Alojamientos");

        //Ingresamos El Cairo en la caja de busqueda Destino
        WebElement destino = driver.findElement(By.cssSelector("div.input-container .sbox-destination"));
        destino.sendKeys("El Cairo");
        //Esperamos a que se despliegue la lista y le damos ENTER a la primera opcion
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.item")));
        destino.sendKeys(Keys.ENTER);


        //Click en la seccion Habitaciones
        driver.findElement(By.cssSelector("div.sbox-distri-container")).click();
        //Click boton - en la seccion adultos
        driver.findElement(By.cssSelector("a.steppers-icon-left ")).click();
        //Click boton Aplicar
        driver.findElement(By.cssSelector("a._pnlpk-apply-button")).click();

        //Click en el checkbox Todavía no he decidido la fecha
        driver.findElement(By.xpath("//label[@class='checkbox-label']")).click();
        //Click en el boton Buscar
        driver.findElement(By.xpath("//em[contains(text(),'Buscar')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[starts-with(@class,'checkbox-label')]")));
        //Filtramos los resultados por Wi-fi gratis en zonas comunes
        driver.findElement(By.xpath("//em[contains(text(),'Wi-Fi gratis en zonas comunes')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'Wi-Fi gratis en zonas comunes')]")));
        //Filtramos los resultados por precio: menor a mayor
        Select s = new Select(driver.findElement(By.id("sorting")));
        s.selectByVisibleText("Precio: menor a mayor");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hotels")));

        //Click en el alojamiento ingresado y cambio de pestana
        busquedaAlojamiento("Ramses Hilton");

        //Click en Ver comentarios
        driver.findElement(By.cssSelector("a.scroll-to-reviews")).click();
        //Seleccionamos la seccion En solitario
        driver.findElement(By.xpath("//label[contains(text(),'En solitario')]")).click();

        //Capturamos el titulo del alojamiento seleccionado
        String TituloHotel = driver.findElement(By.xpath("//span[@class='accommodation-name eva-3-h2']")).getText();
        String checkTituloHotel = "Ramses Hilton";

        //Capturamos subtitulo de comentario filtrado por En solitario
        String comentarioSolitario = driver.findElement(By.xpath("//span[contains(text(),'Viajó solo/a ')]")).getText();
        String checkComentarioSolitario = "Viajó solo/a";

        Assert.assertEquals(checkTituloHotel,TituloHotel);
        Assert.assertEquals(checkComentarioSolitario,comentarioSolitario);
    }

    @Test
    public void alojamientosBuscarPrecioMaximoElegirAeropuertoDestinoYAgregarTraslado() throws InterruptedException {
        //Obtenemos una lista con todas las categorias y le hacemos click cuando encuentre Alojamientos
        busqueda(categorias,"Alojamientos");

        //Ingresamos montego bay en la caja de busqueda Destino
        WebElement destino = driver.findElement(By.cssSelector("div.input-container .sbox-destination"));
        destino.sendKeys("montego bay");
        //Esperamos a que se despliegue la lista y le damos ENTER a la primera opcion
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.item")));
        destino.sendKeys(Keys.ENTER);

        //click en la caja de Entrada de la seccion fechas
        driver.findElement(By.cssSelector("div.input-container .sbox-checkin-date")).click();
        //Obtenemos la lista de las fechas disponibles, Luego clickeamos la fecha ingresada de Entrada y luego la fecha ingresada de Salida
        //Luego click en aplicar
        busquedaFecha("1","8");

        //Click en la seccion Habitaciones
        driver.findElement(By.cssSelector("div.sbox-distri-container")).click();

        //Agrega la cantidad ingresada en la seccion menores
        agregarMenores(3);


        //Agregamos la edad de los menores uno por uno
        for (int i = 0; i < 3; i++) {
            driver.findElements(By.xpath("//div[@class='_pnlpk-minors-age-select-wrapper']//select[@class='select-tag']")).get(i).click();
            driver.findElements(By.xpath("//select[@class='select-tag'] //option[@value='4']")).get(i).click();
        }

        //Click boton Aplicar
        driver.findElement(By.cssSelector("a._pnlpk-apply-button")).click();
        //Click boton Buscar
        driver.findElement(By.xpath("//em[contains(text(),'Buscar')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[starts-with(@class,'checkbox-label')]")));
        //Filtramos los resultados por moneda Dolar
        Select s = new Select(driver.findElement(By.xpath("//div[@class='currency-selection'] //select[@class='select-tag']")));
        s.selectByVisibleText("Dólar");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[starts-with(@class,'checkbox-label')]")));

        //Filtramos el precio maximo
        driver.findElements(By.xpath("//input[@class='input-tag' and @type='number']")).get(1).sendKeys("5000");
        //Click en aplicar el filtro de precio
        driver.findElement(By.xpath("//button[@class='eva-3-btn -md -primary']//following::em[1]")).click();

        //Si se despliega una ventana emergente la cerramos
        if(driver.findElement(By.xpath("//div[contains(@class,'tooltip-container -eva-3-shadow-1')]"))!=null) {
            driver.findElement(By.xpath("//i[contains(@class,'tooltip-close eva-3-icon-close')]")).click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'results-cluster-container')]")));
        //Click en el alojamiento ingresado y cambio de pestana
        busquedaAlojamientoGlobal("Iberostar Rose Hall Beach");


        //Capturamos el titulo del alojamiento seleccionado
        String TituloHotel = driver.findElement(By.xpath("//*[@class='accommodation-name eva-3-h2']")).getText();
        String checkTituloHotel = "Iberostar Rose Hall Beach";

        //click en el boton Reservar Ahora
        driver.findElement(By.xpath("//button[@class='eva-3-btn -md -secondary -eva-3-fwidth']//following::em[1]")).click();

        //Cambiar Aeropuerto
        Select s1 = new Select(driver.findElement(By.id("select-test")));
        s1.selectByVisibleText("OCJ, Aeropuerto Boscobel");

        //click en boton buscar
        driver.findElement(By.xpath("//em[contains(text(),'Buscar')]")).click();

        //Validamos que no busca resultados sin especificar la hora
        String msjErrorHora = driver.findElement(By.xpath("//span[text()='Ingresa una hora']")).getText();
        String checkMsjErrorHora = "Ingresa una hora";

        Assert.assertEquals(checkMsjErrorHora,msjErrorHora);
        Assert.assertEquals(checkTituloHotel,TituloHotel);
    }

    @After
    public void close() throws InterruptedException {
        if(driver != null){
            Thread.sleep(4000);
            driver.quit();
        }

    }

    @AfterClass
    public static void closeAll(){
        System.out.println("closeAll :: Cerrar otras conexiones que fueron utilizadas en el test");

    }

    private void busqueda(List<WebElement> lista, String palabra){
        for (WebElement l: lista){
            if (l.getText().contains(palabra)){
                l.click();
                break;
            }
        }
    }

    private void busquedaFecha(String fechaEntrada,String fechaSalida){
        int count = driver.findElements(By.cssSelector("div._dpmg2--months ._dpmg2--available ._dpmg2--date-number")).size();
        Calendar calendario = Calendar.getInstance();

        for (int i = 0; i < count; i++) {
            String fechas = driver.findElements(By.cssSelector("div._dpmg2--months ._dpmg2--available ._dpmg2--date-number")).get(i).getText();
            if(fechas.equals(fechaEntrada)){
                driver.findElements(By.cssSelector("div._dpmg2--months ._dpmg2--available ._dpmg2--date-number")).get(i).click();
                break;
            }
        }
        for (int i = Integer.parseInt(fechaEntrada); i < count; i++) {
            String fechas = driver.findElements(By.cssSelector("div._dpmg2--months ._dpmg2--available ._dpmg2--date-number")).get(i).getText();
            if(fechas.equals(fechaSalida)){
                driver.findElements(By.cssSelector("div._dpmg2--months ._dpmg2--available ._dpmg2--date-number")).get(i).click();
                driver.findElement(By.xpath("//em[contains(text(),'Aplicar')]")).click();
                break;
            }
        }
    }

    private void busquedaAlojamiento(String alojamiento) throws InterruptedException {
        Thread.sleep(1500);
        driver.findElement(By.xpath("//a[contains(text(),'"+alojamiento+"')]")).click();
        Thread.sleep(1500);
        selectPestana(1);
        }
    private void busquedaAlojamientoGlobal(String alojamiento) throws InterruptedException {
        Thread.sleep(1500);
        driver.findElement(By.xpath("//*[contains(text(),'"+alojamiento+"')]")).click();
        Thread.sleep(1500);
        selectPestana(1);
    }


    public void selectPestana(int index) {
        tabs2 = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(index));
    }

    public void agregarMenores(int menores) {
        for (int i = 0; i < menores; i++) {
            driver.findElement(By.xpath("//div[@class='_pnlpk-itemRow__item _pnlpk-stepper-minors -medium-down-to-lg'] //a[@class='steppers-icon-right sbox-3-icon-plus']")).click();
        }
    }


}





