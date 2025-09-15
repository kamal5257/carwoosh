package com.carwoosh.constant;

public class StartupStatus {


    public enum Stage {
        IDEA("Idea"),
        VALIDATION("Validation"),
        GROWTH("Growth"),
        MATURITY("Maturity"),
        EXIT("Exit");

        private final String description;

        Stage(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum Industry {
        TECH("Technology"),
        HEALTHCARE("Healthcare"),
        FINANCE("Finance"),
        EDUCATION("Education"),
        ECOMMERCE("E-commerce");

        private final String description;

        Industry(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum FundingType {
        SEED("Seed"),
        SERIES_A("Series A"),
        SERIES_B("Series B"),
        ANGEL("Angel"),
        VC("Venture Capital");

        private final String description;

        FundingType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private String name;
    private Stage stage;
    private Industry industry;
    private FundingType fundingType;

    public StartupStatus(String name, Stage stage, Industry industry, FundingType fundingType) {
        this.name = name;
        this.stage = stage;
        this.industry = industry;
        this.fundingType = fundingType;
    }

    @Override
    public String toString() {
        return String.format("Startup: %s, Stage: %s, Industry: %s, Funding Type: %s",
                name, stage.getDescription(), industry.getDescription(), fundingType.getDescription());
    }

    // Example usage
    public static void main(String[] args) {
        StartupStatus myStartup = new StartupStatus("InnovateHealth", Stage.GROWTH, Industry.HEALTHCARE, FundingType.SERIES_A);
        System.out.println(myStartup);
    }
}

