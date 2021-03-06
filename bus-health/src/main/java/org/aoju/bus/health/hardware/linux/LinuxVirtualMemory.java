/*********************************************************************************
 *                                                                               *
 * The MIT License                                                               *
 *                                                                               *
 * Copyright (c) 2015-2020 aoju.org and other contributors.                      *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 ********************************************************************************/
package org.aoju.bus.health.hardware.linux;

import org.aoju.bus.health.Builder;
import org.aoju.bus.health.Memoizer;
import org.aoju.bus.health.common.linux.ProcUtils;
import org.aoju.bus.health.hardware.AbstractVirtualMemory;

import java.util.List;
import java.util.function.Supplier;

/**
 * Memory obtained by /proc/meminfo and /proc/vmstat
 *
 * @author Kimi Liu
 * @version 5.6.8
 * @since JDK 1.8+
 */
public class LinuxVirtualMemory extends AbstractVirtualMemory {

    private final Supplier<MemInfo> memInfo = Memoizer.memoize(this::queryMemInfo, Memoizer.defaultExpiration());

    private final Supplier<VmStat> vmStat = Memoizer.memoize(this::queryVmStat, Memoizer.defaultExpiration());

    @Override
    public long getSwapUsed() {
        return memInfo.get().used;
    }

    @Override
    public long getSwapTotal() {
        return memInfo.get().total;
    }

    @Override
    public long getSwapPagesIn() {
        return vmStat.get().pagesIn;
    }

    @Override
    public long getSwapPagesOut() {
        return vmStat.get().pagesOut;
    }

    private MemInfo queryMemInfo() {
        long swapFree = 0L;
        long swapTotal = 0L;

        List<String> procMemInfo = Builder.readFile(ProcUtils.getProcPath() + "/meminfo");
        for (String checkLine : procMemInfo) {
            String[] memorySplit = Builder.whitespaces.split(checkLine);
            if (memorySplit.length > 1) {
                switch (memorySplit[0]) {
                    case "SwapTotal:":
                        swapTotal = parseMeminfo(memorySplit);
                        break;
                    case "SwapFree:":
                        swapFree = parseMeminfo(memorySplit);
                        break;
                    default:
                        // do nothing with other lines
                        break;
                }
            }
        }
        return new MemInfo(swapTotal, swapTotal - swapFree);
    }

    private VmStat queryVmStat() {
        long swapPagesIn = 0L;
        long swapPagesOut = 0L;
        List<String> procVmStat = Builder.readFile(ProcUtils.getProcPath() + "/vmstat");
        for (String checkLine : procVmStat) {
            String[] memorySplit = Builder.whitespaces.split(checkLine);
            if (memorySplit.length > 1) {
                switch (memorySplit[0]) {
                    case "pgpgin":
                        swapPagesIn = Builder.parseLongOrDefault(memorySplit[1], 0L);
                        break;
                    case "pgpgout":
                        swapPagesOut = Builder.parseLongOrDefault(memorySplit[1], 0L);
                        break;
                    default:
                        // do nothing with other lines
                        break;
                }
            }
        }
        return new VmStat(swapPagesIn, swapPagesOut);
    }

    /**
     * Parses lines from the display of /proc/meminfo
     *
     * @param memorySplit Array of Strings representing the 3 columns of /proc/meminfo
     * @return value, multiplied by 1024 if kB is specified
     */
    private long parseMeminfo(String[] memorySplit) {
        if (memorySplit.length < 2) {
            return 0L;
        }
        long memory = Builder.parseLongOrDefault(memorySplit[1], 0L);
        if (memorySplit.length > 2 && "kB".equals(memorySplit[2])) {
            memory *= 1024;
        }
        return memory;
    }

    private static final class MemInfo {
        private final long total;
        private final long used;

        private MemInfo(long total, long used) {
            this.total = total;
            this.used = used;
        }
    }

    private static final class VmStat {
        private final long pagesIn;
        private final long pagesOut;

        private VmStat(long pagesIn, long pagesOut) {
            this.pagesIn = pagesIn;
            this.pagesOut = pagesOut;
        }
    }
}
