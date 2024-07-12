package org.example.service;

import org.example.exception.RentalServiceException;
import org.example.model.RentalTerms;
import org.example.model.Tool;

public interface RentalService {
    public RentalTerms checkout(String toolCode, String date, int duration, int discountPercent) throws RentalServiceException;
    public void addTool(Tool tool);
    public void removeTool(String toolCode);

}