package microservices.book.mandelbrot.controller;


import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @PostMapping("/calcParallel")
    ResponseEntity<Calculation> calcParallel(@RequestBody CalcParameters calcParameters) {
        Calculation calculation = calculationService.performParallelCalculation(calcParameters);
        return new ResponseEntity<>(calculation, HttpStatus.OK);
    }

    @PostMapping("/save")
    ResponseEntity<String> saveCalculation(@RequestBody Calculation calculation) {
        calculationService.saveCalculationToDatabase(calculation);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/getCalculation/{id}")
    ResponseEntity<Calculation> getCalculationById(@PathVariable Long id) {
        Calculation result = calculationService.getCalculationById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllCalculationLight")
    ResponseEntity<List<CalculationsRepresentation>> getAllCalculations() {
        List<CalculationsRepresentation> getAllCalculationsRepresentations =
                calculationService.getAllCalculationsRepresentations();

        return new ResponseEntity<>(getAllCalculationsRepresentations, HttpStatus.OK);
    }

    @PostMapping("/test")
    ResponseEntity<Calculation> testParams(@RequestBody CalcParameters calcParameters) {
        CalcResult calcResult = calculationService.calculateIntArea(calcParameters);
        Calculation calculation = new Calculation(calcParameters, calcResult, new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(calculation, HttpStatus.CREATED);
    }

}
