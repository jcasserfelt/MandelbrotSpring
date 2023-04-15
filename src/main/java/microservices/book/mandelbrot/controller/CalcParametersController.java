package microservices.book.mandelbrot.controller;


import microservices.book.mandelbrot.domain.*;
import microservices.book.mandelbrot.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fractal")
final class CalcParametersController {

    private final CalculationService calculationService;

    @Autowired
    CalcParametersController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/calcMandelbrot")
    ResponseEntity<Calculation> calcParallel(@RequestBody CalcParameters calcParameters) {
        Calculation calculation = calculationService.performParallelMandelbrotCalculation(calcParameters);
        return new ResponseEntity<>(calculation, HttpStatus.OK);
    }

    @PostMapping("/calcJulia")
    ResponseEntity<Calculation> calcJulia(@RequestBody CalcParameters calcParameters) {
        Calculation calculation = calculationService.performParallelJuliaSetCalculation(calcParameters);
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

}
