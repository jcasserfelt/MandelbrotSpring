# MandelbrotSpring
Web application, visualisation of the Mandelbrot set <br/>
Fractal calulations on server side. Image generated on client side. Tons of fun
<br/>
<br/>
 ![screenshot](ScreenShots/mandelbrot-multiThread.jpg)
##To run the application:
1. clone project
2. set up maven in IDE

3. **(only if you wish to save calculation result to database)** <br/>
Go into h2 console and make sql-statement:
"ALTER TABLE CALC_RESULT ALTER COLUMN RESULT_DATA BLOB" then press RUN  <br/>
 ![screenshot](ScreenShots/h2consoless.jpg ) <br/>


4. Go to http://localhost:8080/ and have some fun 

---

![screenshot](ScreenShots/screenshot.jpg)