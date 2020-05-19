package com.example.halward;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author yuliiamelnyk on 14/05/2020
 * @project Halward
 */
public interface ConvertDataTypes {

    LocalDate convertToLocalDateViaMilisecond(Date dateToConvert);

    Date convertToDateViaInstant(LocalDate dateToConvert);
}
