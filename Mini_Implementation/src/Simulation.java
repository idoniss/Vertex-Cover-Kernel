

public class Simulation {
    public long timeNaive, timeLectureKernel, timeBookKernel, simulateCounter;


    public Simulation(){
        this.timeNaive = 0;
        this.timeLectureKernel = 0;
        this.timeBookKernel = 0;
        this.simulateCounter = 0;
    }
    public Simulation(long naive, long lectureKernel, long bookKernel){
        this.timeNaive = naive;
        this.timeLectureKernel = lectureKernel;
        this.timeBookKernel = bookKernel;
    }

    public void addSimulation(long naive, long lectureKernel, long bookKernel){
        simulateCounter++;
        timeNaive = ((timeNaive*(simulateCounter - 1)) + naive) / simulateCounter;
        timeLectureKernel = ((timeNaive*(simulateCounter - 1)) + lectureKernel) / simulateCounter;
        timeBookKernel = ((timeNaive*(simulateCounter - 1)) + bookKernel) / simulateCounter;
    }
}
