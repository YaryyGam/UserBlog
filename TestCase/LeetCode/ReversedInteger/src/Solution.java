//Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
//
//Assume the environment does not allow you to store 64-bit integers (signed or unsigned).
//
//
//
//Example 1:
//
//Input: x = 123
//Output: 321
//Example 2:
//
//Input: x = -123
//Output: -321
//Example 3:
//
//Input: x = 120
//Output: 21
//
//
//Constraints:
//
//        -231 <= x <= 231 - 1

public class Solution {
    public Solution() {
    }

    public int reverse(int x) {
        int res = 0;

        while(x != 0) {
            int element = x % 10;
            x /= 10;
            if (res <= 214748364 && (res != 214748364 || element <= 7)) {
                if (res >= -214748364 && (res != -214748364 || element >= -8)) {
                    res = res * 10 + element;
                    continue;
                }

                return 0;
            }

            return 0;
        }

        return res;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.reverse(64623));
    }
}

