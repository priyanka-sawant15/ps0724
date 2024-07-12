package org.example.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import org.example.exception.InvalidDiscountException;
import org.example.exception.InvalidDurationException;
import org.example.exception.RentalServiceException;
import org.example.model.RentalTerms;
import org.example.model.Tool;
import org.example.model.ToolBox;
import org.example.service.RentalService;

public class RentalServiceImpl implements RentalService {
    private ToolBox toolBox;
    private DateTimeFormatter dateFormatter;
    private static final String DATE_FORMAT_SHORT = "M/d/yy";
    private static final String DATE_FORMAT_MEDIUM = "M/d/yyyy";

    public RentalServiceImpl() {
        toolBox = new ToolBox();
        dateFormatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern(DATE_FORMAT_MEDIUM))
                .appendOptional(DateTimeFormatter.ofPattern(DATE_FORMAT_SHORT))
                .toFormatter();
    }

    @Override
    public RentalTerms checkout(String toolCode, String date, int duration, int discountPercent) throws RentalServiceException {
        if (duration  < 1) {
            throw new InvalidDurationException("The duration of the rental must be more than one day." +
                    " The duration provided was: " + duration);
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("The discount must be a whole number between 0 and 100." +
                    " The discount provided was:" + discountPercent);
        }
        var tool = toolBox.getTool(toolCode);
        if (tool == null) {
            throw new RentalServiceException("The tool you are looking for with code " +
                    toolCode + " does not exist.");
        }
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date, dateFormatter);
        }catch(DateTimeParseException e) {
            throw new RentalServiceException("The date: " + date + " is in an unrecognized format." +
                    " Try m/d/yy or m/d/yyyy.");
        }

        return createAgreement(tool, parsedDate, duration, discountPercent);
    }

    private RentalTerms createAgreement(Tool tool, LocalDate date, int duration, int discountPercent) {
        RentalTerms agreement = new RentalTerms(tool.getToolCode());
        agreement.setToolType(tool.getToolType());
        agreement.setToolBrand(tool.getBrand());
        agreement.setRentalDays(duration);
        agreement.setCheckoutDate(date);

        var chargeDays = tool.calculateChargeDays(date, duration);
        agreement.setChargeDays(chargeDays);

        agreement.setDailyRentalCharge(tool.getDailyCharge());

        var preDiscountCharge = tool.calculateCharge(chargeDays);
        agreement.setPreDiscountCharge(preDiscountCharge);

        agreement.setDiscountPercent(discountPercent);

        var discountPercentDecimal = discountPercent / 100.0F;
        var discountAmount = (discountPercentDecimal * preDiscountCharge);
        discountAmount = Math.round(discountAmount * 100.F) / 100.F;
        agreement.setDiscountAmount(discountAmount);

        agreement.setFinalCharge(preDiscountCharge-discountAmount);
        agreement.setDueDate(date.plusDays(duration));

        return agreement;

    }

    @Override
    public void addTool(Tool tool) {
        toolBox.addTool(tool);
    }

    @Override
    public void removeTool(String toolCode) {
        toolBox.removeTool(toolCode);
    }
}
