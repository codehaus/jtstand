/* 
 * File:   main.cpp
 * Author: albert
 *
 * Created on September 12, 2010, 12:55 PM
 */

#include <iostream>
#include <iterator>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>

using namespace std;

class CVSRow {
public:

    std::string const& operator[](std::size_t index) const {
        return m_data[index];
    }

    std::size_t size() const {
        return m_data.size();
    }

    void readNextRow(std::istream& str) {
        std::string line;
        std::getline(str, line);

        std::stringstream lineStream(line);
        std::string cell;

        m_data.clear();
        while (std::getline(lineStream, cell, ',')) {
            m_data.push_back(cell);
        }
    }
private:
    std::vector<std::string> m_data;
};

std::istream & operator>>(std::istream& str, CVSRow& data) {
    data.readNextRow(str);
    return str;
}

const char *usage = "required one argument: the file name!";

int main(int argc, char* argv[]) {
    cout << "Hello world again!" << endl;

    cout << "argc = " << argc << endl;
    for (int i = 0; i < argc; i++)
        cout << "argv[" << i << "] = " << argv[i] << endl;

    if (argc != 2) {
        cout << usage;
        return 1;
    }

    std::ifstream file(argv[1]);
    cout << "file:" << argv[1] << " " << (file ? "exists" : "missing") << endl;

    CVSRow row;

    while (file >> row) {
        std::cout << "4th Element(" << row[3] << ")\n";
    }
    return 0;
}
