package com.github.EnzoMendes34.exceptions;

import java.util.Date;

public record ExceptionResponse(Date timeStamp, String message, String details) {}
