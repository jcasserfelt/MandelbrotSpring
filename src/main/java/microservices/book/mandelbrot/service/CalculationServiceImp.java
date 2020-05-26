package microservices.book.mandelbrot.service;

import lombok.Getter;
import lombok.Setter;
import microservices.book.mandelbrot.domain.CalcParameters;
import microservices.book.mandelbrot.domain.Calculation;
import microservices.book.mandelbrot.domain.User;
import microservices.book.mandelbrot.repository.CalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationServiceImp implements CalculationService {

    private CalculationRepository calculationRepository;


    @Autowired
    public CalculationServiceImp(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Override
    public byte calculatePoint(double x, double y, int iterations) {
        double cx = x;
        double cy = y;

        int i = 0;
        for (i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

//            double resultValue = x * x + y * y;
            if (x * x + y * y > 2) {
                return (byte) i;
            }
        }
        if (i == iterations) {
            return (byte) iterations;
        }
        return (byte) iterations;
    }

    @Override
    public int calculateIntPoint(double x, double y, int iterations) {
        double cx = x;
        double cy = y;

        int i = 0;
        for (i = 1; i <= iterations; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;

            double resultValue = x * x + y * y;
            if (x * x + y * y > 2) {
                return i;
            }
        }
        if (i == iterations) {
            return iterations;
        }
        return iterations;
    }

    @Override
    public int[] calculateIntArea(CalcParameters parameters) {
        List<InnerCoords> coordinates = makeCoordinates(parameters);
        List<Integer> resultList = new ArrayList<>();
        int[] resultArray = new int[coordinates.size()];

        int counter = 0;
        try {
            for (InnerCoords tempCoordinate : coordinates) {
                int temp = calculateIntPoint(tempCoordinate.xVal, tempCoordinate.yVal, parameters.getInf_n());
                resultList.add(temp);
                resultArray[counter] = temp;
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
            e.printStackTrace();
        }
        return resultArray;
    }

    @Override
    public List<Byte> calculateArea(CalcParameters parameters) {
        // list med inner cords
        // skapa inner coords mha makeCoords()
        List<InnerCoords> coordinates = makeCoordinates(parameters);
        List<Byte> resultList = new ArrayList<>();

        // använd calculatesubarea(inner coords lista)
        int counter = 0;
        try {
            for (InnerCoords tempCoordinate : coordinates) {
                byte temp = calculatePoint(tempCoordinate.xVal, tempCoordinate.yVal, parameters.getInf_n());
                resultList.add(temp);
//                subResultArray[counter] = this.convertToPGMRangeByte(calculatePoint2(tempCoordinate.x, tempCoordinate.y, inf_n), inf_n);
//                subResultArray[counter] = this.convertToPGMRangeByte(temp, inf_n);
                counter++;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index vid outofbounce: " + counter);
            e.printStackTrace();
        }

        return resultList;
    }


    @Override
    public List<InnerCoords> makeCoordinates(CalcParameters p) {

        ArrayList<InnerCoords> coordinates = new ArrayList<>();

        double xInterval = Math.abs(p.getMax_c_re() - p.getMin_c_re());
        double yInterval = Math.abs(p.getMax_c_im() - p.getMin_c_im());

        double tempX = p.getMin_c_re();
        double tempY = p.getMax_c_im();

        double xAdd;
        double ySub;

        int x = p.getX();
        int y = p.getY();


        // create x*y coordinates
        for (int i = 0; i < y; i++) {
            if (i != 0) {
                ySub = (yInterval / (x - 1));
                tempY = tempY - ySub;
            }
            tempX = p.getMin_c_re();           // restart x value for the next row
            for (int j = 0; j < x; j++) {
                coordinates.add(new InnerCoords(tempX, tempY));
                xAdd = (xInterval / (y - 1));
                tempX = tempX + xAdd;
            }
        }
        return coordinates;
    }

    @Transactional
    @Override
    public String saveCalculationToDatabase(Calculation calculation) {
        calculationRepository.save(calculation);
        return "save successful";
    }

    @Override
    public Calculation getCalculationById(Long id) {
//        Iterable<Calculation> lista = calculationRepository.findAll();
        Calculation result = calculationRepository.findFirstById(id).get();
        return result;
    }

    @Override
    public List<Calculation> getAllCalculations() {
        Iterable<Calculation> resultList = calculationRepository.findAll();
        List<Calculation> calculationList = new ArrayList<>();
        resultList.forEach(calculationList::add);
        return calculationList;
    }

    @Override
    public List<CalculationsRepresentation> getAllCalculationsRepresentations() {
        Iterable<Calculation> resultList = calculationRepository.findAll();
        List<CalculationsRepresentation> calculationsRepresentations = new ArrayList<>();
        for (Calculation c : calculationRepository.findAll()) {
            calculationsRepresentations.add(new CalculationsRepresentation(c));
        }
        return calculationsRepresentations;
    }

    @Getter
    public final class InnerCoords {

        double xVal;
        double yVal;

        public InnerCoords(double xVal, double yVal) {
            this.xVal = xVal;
            this.yVal = yVal;
        }
    }

    @Getter
    @Setter
    public final class CalculationsRepresentation {
        Long id;
        User user;
        CalcParameters calcParameters;
        int[] resultData;
        Timestamp timestamp;


        public CalculationsRepresentation(Calculation calculation) {
            this.id = calculation.getId();
            this.user = calculation.getUser();
            this.calcParameters = calculation.getCalcParameters();
            this.resultData = null;
            this.timestamp = calculation.getTimestamp();
        }
    }
}
