# Set the base image
FROM python:3.11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Pipfile and Pipfile.lock into the container
COPY ../python-api/Pipfile ./
COPY ../python-api/Pipfile.lock ./

# Install system dependencies
RUN apt-get update \
    && apt-get install -y libpq-dev build-essential \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Pipenv
RUN pip install pipenv

# Copy the rest of the application code into the container
COPY ../python-api/app /app/app

COPY ../python-api/main.py /app

# Install the required packages
RUN pipenv install --deploy --system

# Expose port 8000 to the outside world
EXPOSE 8000

# Add a label for the author
LABEL authors="Rahim Ahmed"
