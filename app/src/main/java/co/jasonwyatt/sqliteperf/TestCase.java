package co.jasonwyatt.sqliteperf;

import java.util.List;

/**
 * @author jason
 */

public interface TestCase {
    void resetCase();
    Metrics runCase();

    class Metrics {
        private final String mName;
        private final float mVariable;
        private int mIterations = 1;
        private long mStartTime;
        private long mEndTime;
        private long mElapsedTime;

        public Metrics(String name, float variable) {
            mName = name;
            mVariable = variable;
        }

        Metrics(List<Metrics> iterations) {
            mName = iterations.get(0).mName;
            mVariable = iterations.get(0).mVariable;
            mIterations = iterations.size();
            mElapsedTime = 0;
            for (Metrics m : iterations) {
                mElapsedTime += m.mElapsedTime;
            }
            mElapsedTime = (long) (mElapsedTime*1.0f / iterations.size());
        }

        public void started() {
            mStartTime = System.currentTimeMillis();
        }

        public void finished() {
            mEndTime = System.currentTimeMillis();
            mElapsedTime = mEndTime - mStartTime;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(mName);
            sb.append(":\n");
            if (mIterations > 1) {
                sb.append(mIterations);
                sb.append(" iterations, each averaged ");
            } else {
                sb.append("took ");
            }
            sb.append(((float) mElapsedTime / mIterations) / 1000f);
            sb.append(" seconds");
            return sb.toString();
        }

        public float getVariable() {
            return mVariable;
        }

        public long getElapsedTime() {
            return mElapsedTime;
        }
    }

}
