FROM ubuntu:latest

RUN apt-get -y update && apt-get install -y

RUN apt-get -y install clang
RUN apt-get -y install cmake

# set a directory for the server
WORKDIR /usr/src/server-web

# copy all the files to the container
COPY  . .

# install dependencies
WORKDIR /usr/src/server-web/pistacheLib/build

RUN cmake -G "Unix Makefiles" -DCMAKE_BUILD_TYPE=Release ..
RUN make
RUN make install


WORKDIR /usr/src/server-web/src

RUN clang++ -std=c++14 main.cpp -o main -lpistache -lpthread

EXPOSE 8081

CMD ["./main"]