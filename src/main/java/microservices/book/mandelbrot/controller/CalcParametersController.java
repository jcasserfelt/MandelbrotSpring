package microservices.book.mandelbrot.controller;


import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.service.CalculationService;
import microservices.book.mandelbrot.service.CalculationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/mandelbrot")
final class CalcParametersController {

    private final CalculationService calculationService;

    @Autowired
    CalcParametersController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/test")
    ResponseEntity<Calculation> testParams(@RequestBody CalcParameters calcParameters) {
//        List<Integer> resultInt = calculationService.calculateIntArea(calcParameters);
        long startTime = System.currentTimeMillis();

        CalcResult calcResult = calculationService.calculateIntArea(calcParameters);
        Calculation calculation = new Calculation(calcParameters, calcResult, new Timestamp(new Date().getTime()));
        long calcTime = System.currentTimeMillis() - startTime;
        System.out.println();
        System.out.println("single thread calc time: " + calcTime + "ms");
//        System.out.println("------------------");
//        calculation.getResultObj().calculationTime = calcTime;
        for (int i = 0; i <(System.currentTimeMillis() - startTime) / 400 ; i++) {
            System.out.print("-");
        }
        return new ResponseEntity<>(calculation, HttpStatus.CREATED);
    }

    @PostMapping("/calcParallel")
    ResponseEntity<Calculation> calcParallel(@RequestBody CalcParameters calcParameters) {

//        CalcResult calcResult =  calculationService.calculateIntArea(calcParameters);
//        User user = new User(calcParameters.toString(), "enPassword");
        long startTime = System.currentTimeMillis();
        Calculation calculation = calculationService.performParallelCalculation(calcParameters);
        System.out.println();
        System.out.println(calcParameters.getDivider() + "x multi thread calc time: " + (System.currentTimeMillis() - startTime) + "ms");
        for (int i = 0; i <(System.currentTimeMillis() - startTime) / 400 ; i++) {
            System.out.print("-");
        }
//        System.out.println("------------------");

        return new ResponseEntity<>(calculation, HttpStatus.CREATED);
    }

    @PostMapping("/save")
    ResponseEntity<String> saveCalculation(@RequestBody Calculation calculation) {
        calculationService.saveCalculationToDatabase(calculation);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PostMapping("/getCalculation")
    ResponseEntity<Calculation> getCalculationById(@RequestBody Long id) {
        Calculation result = calculationService.getCalculationById(id);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/getAllCalculationLight")
    ResponseEntity<List<CalculationsRepresentation>> getAllCalculations() {

        List<CalculationsRepresentation> getAllCalculationsRepresentations =
                calculationService.getAllCalculationsRepresentations();

        return new ResponseEntity<>(getAllCalculationsRepresentations, HttpStatus.CREATED);
    }

}
